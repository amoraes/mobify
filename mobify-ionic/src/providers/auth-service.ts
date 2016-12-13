import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';

import { CONFIG } from '../app/config';

@Injectable()
export class AuthService {

  private data:any;
  
  constructor(public http: Http) {
   
  }

  public login(username:string, password:string, reconnect:boolean): Promise<any> {
    return new Promise(resolve => {
      var url = CONFIG.oauth_base_url + CONFIG.oauth_token;
      var headers = new Headers();
      headers.append("Authorization", "Basic " + CONFIG.client_id + ":" + CONFIG.client_secret);
      headers.append('Content-Type', 'application/x-www-form-urlencoded');
      var params = "grant_type=password&username="+username+"&password="+password;
      this.http.post(url, params, { "headers": headers })
        .map(res => res.json())
        .subscribe(data => {
          // we've got back the raw data, now generate the core schedule data
          // and save the data for later reference
          this.data = data;
          console.log(JSON.stringify(this.data));
          resolve(this.data);
        });
      });
  }
  
}
