import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { BasicService } from './basic-service';

import { User } from '../model/user';

import { CONFIG } from '../app/config';

/**
 * This service class do all the work related to the OAuth authentication flow
 */
@Injectable()
export class AuthService extends BasicService {

  /**
   * The currently logged in user
   */
  private user:User;  
  
  constructor(public http: Http) {
    super();
  }
  
  /**
   * Verifies if there is an user logged in
   */
  public isUserLoggedIn():boolean {
    return (this.user != null);
  }
  
  /**
   * Returns the current user
   */
  public getUser(): User {
    return this.user;
  }
  
  public login(username:string, password:string): Promise<any> {
    let url = CONFIG.oauth_base_url + CONFIG.oauth_token;
    let headers = new Headers();
    headers.append("Authorization", "Basic " + btoa(CONFIG.client_id + ":" + CONFIG.client_secret));
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    let params = "grant_type=password&username="+username+"&password="+password;
    return this.http.post(url, params, { headers: headers })
      .toPromise()
      .then(
        res => {
          console.log(res.json());
          if(res.status == 200){
            let user:User = new User();
            user.accessToken = res.json()[CONFIG.token_access_token_key];
            user.refreshToken = res.json()[CONFIG.token_refresh_token_key];
            //expiration timestamp
            if(CONFIG.token_expiration_type == 'seconds'){
              user.accessTokenValidity = (new Date).getTime() + (res.json()[CONFIG.token_expiration_key] * 1000);
            }else{ //milliseconds
              user.accessTokenValidity = (new Date).getTime() + (res.json()[CONFIG.token_expiration_key]);
            }
            let url = CONFIG.oauth_base_url + CONFIG.oauth_user;
            let headers = new Headers();
            headers.append("Authorization", "Bearer " + user.accessToken);
            return this.http.get(url, { headers: headers })
            .toPromise()
            .then(
              res => {
                if(res.status == 200){
                  console.log(res.json());
                  user.username = res.json().name;
                  this.user = user;
                  return true;
                }else{
                  return false;
                }
              }
            )
            .catch(this.handleError);
          }else{
            return false;  
          }
        }
      )
      .catch(this.handleError);
  }
  
}
