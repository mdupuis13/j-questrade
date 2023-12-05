# Example `cUrl` calls

## GET access-token
```bash
curl -i --request GET --url 'http://localhost:8080/oauth2/token?grant_type=refresh_token&refresh_token=my-lone-refresh-token'
```

## GET accounts
```bash
curl -i --request GET --header 'Authorization: Bearer my-access-token-123'  --url 'http://localhost:8080/v1/accounts'
```
