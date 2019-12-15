## Authentication and Authorization Server

### Create User in MySql:
```
create user admin@localhost identified by 'admin123';
GRANT ALL PRIVILEGES ON * . * TO admin@localhost;
FLUSH PRIVILEGES;

```

### Auth0 SSO Provider

```


# Application Name: k8cluster
# UserName: amit.kshirsagar.13@gmail.com
# Domain: k8cluster.auth0.com

```