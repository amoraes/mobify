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
  token_expiration_type: "seconds", //could be in milliseconds to  
  //mobify restful api base url
  mobify_api_base_url: "http://localhost:8080/mobify/api", 
  //url to obtain a ticket to open a websocket connection
  mobify_websocket_ticket_url: "http://localhost:8080/mobify/ws/ticket",
  //websocket url
  mobify_websocket_url: "ws://localhost:8080/mobify/ws/notifications",
  //websocket (sockjs) url
  mobify_websocket_sockjs_url: "http://localhost:8080/mobify/sockjs/notifications",


}