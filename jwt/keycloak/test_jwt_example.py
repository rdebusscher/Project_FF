import requests
import keycloak_config


def get_user_access_token(user_name, password):
    token_url = f"{keycloak_config.KEYCLOAK_SERVER}/realms/{keycloak_config.REALM_NAME}/protocol/openid-connect/token"
    token_data = {
        "username": user_name,
        "password": password,
        "grant_type": "password",
        "client_id": "ff_school_client"
    }
    token_response = requests.post(token_url, data=token_data)
    token_response.raise_for_status()

    return token_response.json()["access_token"]


def call_endpoint_get(path, token):
    url = "http://localhost:8080"+path
    client_headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }
    response = requests.get(url, headers=client_headers)
    response.raise_for_status()
    return response.content


def test_endpoints(user_name, passw):
    # Obtain access token from token endpoint
    token = get_user_access_token(user_name, passw)

    print(f"Response to user endpoint for {user_name}")
    print(call_endpoint_get("/protected/user", token))
    print(f"Response to admin endpoint for {user_name}")
    print(call_endpoint_get("/protected/admin", token))


if __name__ == '__main__':
    test_endpoints("admin_ff", "admin_pw")
    test_endpoints("student1", "student1_pw")
