# Example `cUrl` calls

## GET access-token
```bash
curl -i --request GET --url 'http://localhost:8080/oauth2/token?grant_type=refresh_token&refresh_token=my-lone-refresh-token'
```
Example response:

    HTTP/1.1 200 OK
    Content-Type: application/json; charset=utf-8
    Date: Mon., 04 Dec. 2023 21:08:56 EST
    Matched-Stub-Id: 239aa8c2-3ac0-4ad1-bb5d-fef8bbe397c2
    Transfer-Encoding: chunked
    
    {"access_token":"my-access-token-123","api_server":"https://api03.iq.questrade.com/","expires_in":1800,"refresh_token":"xyz-refresh_token-987","token_type":"Bearer"}

## GET accounts
```bash
curl -i --request GET --header 'Authorization: Bearer my-access-token-123'  --url 'http://localhost:8080/v1/accounts'
```

Example response:

    HTTP/1.1 200 OK
    Content-Type: application/json; charset=utf-8
    Date: Tue, 05 Dec 2023 01:34:24 GMT
    x-ratelimit-remaining: 29999
    Matched-Stub-Id: f288e348-7606-4ab2-96f4-273f1bacdeda
    Transfer-Encoding: chunked
    
    {"accounts":[{"type":"TFSA","number":"99912345","status":"Active","isPrimary":true,"isBilling":true,"clientAccountType":"Individual"},{"type":"RRSP","number":"99912346","status":"Active","isPrimary":false,"isBilling":false,"clientAccountType":"Individual"}],"userId":999123}