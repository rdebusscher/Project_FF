# Set the Keycloak server URL and credentials
KEYCLOAK_SERVER = "http://localhost:8888/auth"  # Depends on version of keycloak, version 17+ is using / by default (instead of /auth)

# Set the realm name
REALM_NAME = "atbash_project_ff"
CLIENT_NAME = "ff_school_client"