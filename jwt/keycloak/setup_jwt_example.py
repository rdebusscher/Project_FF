import requests

# Set the Keycloak server URL and credentials
KEYCLOAK_SERVER = "http://localhost:8888/auth"  # Depends on version, version 17+ is using / by default (instead of /auth)
KEYCLOAK_USER = "atbash"
KEYCLOAK_PASSWORD = "secret"

# Set the realm name
REALM_NAME = "atbash_project_ff"
CLIENT_NAME = "ff_school_client"


def get_super_user_access_token():
    """
    Get a Access Token for the super user that allows the configuration of the Keycloak environment.
    :return: BASE64 encoded of JSON Access token
    """
    token_url = f"{KEYCLOAK_SERVER}/realms/master/protocol/openid-connect/token"
    token_data = {
        "username": KEYCLOAK_USER,
        "password": KEYCLOAK_PASSWORD,
        "grant_type": "password",
        "client_id": "admin-cli"
    }
    token_response = requests.post(token_url, data=token_data)
    token_response.raise_for_status()
    return token_response.json()["access_token"]


def create_realm(access_token):
    """
    Create a realm for our test application
    :param access_token: The access token of the superuser user that is allowed to perform the action
    """
    realm_url = f"{KEYCLOAK_SERVER}/admin/realms"
    realm_data = {
        "realm": REALM_NAME,
        "enabled": True,
        "displayName": "Atbash Project FF realm",
        "accessTokenLifespan": 1800  # 30 minutes in seconds
    }
    realm_headers = {
        "Authorization": f"Bearer {access_token}",
        "Content-Type": "application/json"
    }
    realm_response = requests.post(realm_url, headers=realm_headers, json=realm_data)
    realm_response.raise_for_status()


def create_client(access_token):
    """
    Create a Client for our test application
    :param access_token: The access token of the superuser that is allowed to perform the action
    :return: client-id for the newly created client.
    """
    client_url = f"{KEYCLOAK_SERVER}/admin/realms/{REALM_NAME}/clients"
    client_data = {
        "clientId": CLIENT_NAME,
        "enabled": True,
        "protocol": "openid-connect",
        "publicClient": True,
        "bearerOnly": False,
        "standardFlowEnabled": True,
        "implicitFlowEnabled": False,
        "directAccessGrantsEnabled": True,
        "serviceAccountsEnabled": False,
        "authorizationServicesEnabled": False,
        "redirectUris": ["http://localhost:8080/*"]
    }
    client_headers = {
        "Authorization": f"Bearer {access_token}",
        "Content-Type": "application/json"
    }
    client_response = requests.post(client_url, headers=client_headers, json=client_data)
    client_id = None
    if client_response.status_code == 201:
        # retrieve the Location header
        location_header = client_response.headers.get('Location')
        # extract the user id from the Location header
        client_id = location_header.split('/')[-1]
        print(f"Created Client {CLIENT_NAME} with id: {client_id}")
    else:
        print("Client creation failed.")
        print(client_response.content)
    return client_id


def add_mapper(access_token, client_id):
    """
    Create a Mapper that adds Realm Roles to the Access token as claim 'groups'
    :param access_token: The access token of the superuser that is allowed to perform the action
    :param client_id: Identification of the Client to which we add the mapper.
    :return:
    """
    mapper_url = f"{KEYCLOAK_SERVER}/admin/realms/{REALM_NAME}/clients/{client_id}/protocol-mappers/models"
    mapper_data = {
        "config": {
            "access.token.claim": "true",
            "claim.name": "groups",  # to make it MicroProfile compliant
            "jsonType.label": "String",
            "multivalued": "true",
            "user.attribute": "roles"
        },
        "name": "realm roles",
        "protocol": "openid-connect",
        "protocolMapper": "oidc-usermodel-realm-role-mapper"
    }

    realm_headers = {
        "Authorization": f"Bearer {access_token}",
        "Content-Type": "application/json"
    }

    mapper_response = requests.post(mapper_url, headers=realm_headers, json=mapper_data)
    mapper_response.raise_for_status()


def create_user(access_token, user_name, password, first_name, last_name):
    """
    Create a user within the realm.
    :param access_token: The access token of the superuser that is allowed to perform the action
    :param user_name: The username to create
    :param password: The password
    :param first_name: First name for the user
    :param last_name: Last name for the user
    :return: user Id of created user
    """
    user_url = f"{KEYCLOAK_SERVER}/admin/realms/{REALM_NAME}/users"
    realm_data = {
        "username": user_name,
        "email": f"{user_name}@ff.atbash.be",
        "firstName": first_name,
        "lastName": last_name,
        "enabled": True,
        "credentials": [{
            "value": password,
            "type": "password"
        }]
    }
    realm_headers = {
        "Authorization": f"Bearer {access_token}",
        "Content-Type": "application/json"
    }
    user_response = requests.post(user_url, headers=realm_headers, json=realm_data)
    user_id = None
    if user_response.status_code == 201:
        # retrieve the Location header
        location_header = user_response.headers.get('Location')
        # extract the user id from the Location header
        user_id = location_header.split('/')[-1]
        print(f"Created user {user_name} with id: {user_id}")
    else:
        print("User creation failed.")
        print(user_response.content)
    return user_id


def create_role(access_token, role, role_description):
    """
    Create a Realm role.
    :param access_token: The access token of the superuser that is allowed to perform the action
    :param role:  Role name
    :param role_description: role description
    """
    role_url = f"{KEYCLOAK_SERVER}/admin/realms/{REALM_NAME}/roles"
    realm_data = {
        "name": role,
        "description": role_description
    }

    realm_headers = {
        "Authorization": f"Bearer {access_token}",
        "Content-Type": "application/json"
    }
    role_response = requests.post(role_url, headers=realm_headers, json=realm_data)
    if role_response.status_code == 201:
        # retrieve the Location header
        location_header = role_response.headers.get('Location')
        # extract the role id from the Location header
        role_id = location_header.split('/')[-1]
        print(f"Created role {role} with id: {role_id}")
    else:
        print("Role creation failed.")
        print(role_response.content)


def get_roles(access_token):
    """
    Get all roles within the realm
    :param access_token: The access token of the superuser that is allowed to perform the action
    :return: dictionary with name - id role mapping
    """
    role_url = f"{KEYCLOAK_SERVER}/admin/realms/{REALM_NAME}/roles"
    realm_headers = {
        "Authorization": f"Bearer {access_token}",
        "Content-Type": "application/json"
    }
    role_response = requests.get(role_url, headers=realm_headers)
    role_response.raise_for_status()
    roles = role_response.json()
    return {item['name']: item['id'] for item in roles}


def add_user_roles(access_token, user_id, role_dict, selected_roles):
    """
    Add the roles within the selected_roles parameter ro the user.
    :param access_token: The access token of the superuser that is allowed to perform the action
    :param user_id: The user id to add the roles for
    :param role_dict: The dictionary with name - id role mapping
    :param selected_roles: The list with roles the user needs
    """
    role_url = f"{KEYCLOAK_SERVER}/admin/realms/{REALM_NAME}/users/{user_id}/role-mappings/realm"
    user_roles = []
    for role in selected_roles:
        user_roles.append({'id': role_dict[role], 'name': role})

    headers = {
        "Authorization": f"Bearer {access_token}",
        "Content-Type": "application/json"
    }
    role_response = requests.post(role_url, headers=headers, json=user_roles)
    role_response.raise_for_status()


if __name__ == '__main__':
    # Obtain access token from token endpoint
    access_token = get_super_user_access_token()

    # Create a new realm using the access token
    create_realm(access_token)

    # Create a new client in the realm
    client_id = create_client(access_token)

    # Add mapper so that roles end up in Access token (JWT)
    add_mapper(access_token, client_id)

    # Create custom roles
    create_role(access_token, "administrator", "Main administrator role")

    # Create new users in the realm
    admin_user_id = create_user(access_token, "admin_ff", "admin_pw", "admin", "ff_school")
    student1_user_id = create_user(access_token, "student1", "student1_pw", "John", "Doe")

    # get Roles mapping (name - unique id)
    role_dict = get_roles(access_token)

    # Assign roles
    add_user_roles(access_token, admin_user_id, role_dict, ["administrator"])
