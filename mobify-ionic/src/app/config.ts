export const CONFIG = {
  //oauth client and secret
  client_id: "mobify",
  client_secret: "aaa111bbb222ccc333",
  //scopes requested
  scope: "openid",
  //oauth server base url
  oauth_base_url: "http://localhost:9999/auth",
  //oauth endpoints 
  oauth_token: "/oauth/token",
  oauth_user: "/user",
  //token response properties
  token_access_token_key: "access_token",
  token_refresh_token_key: "refresh_token",
  token_expiration_key: "expires_in",
  token_expiration_type: "seconds" //could be in milliseconds to  
}