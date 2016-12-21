import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { BasicService } from './basic.service';
import { AuthService } from './auth.service';

import { Application } from '../model/application';

import { CONFIG } from '../app/config';

/**
 * This service class connect to the API to get applications data
 */
@Injectable()
export class ApplicationService extends BasicService {

  constructor(private http: Http, private authService: AuthService) {
    super();
  }
  
  /**
   * Convert an application value object received from the backend to a Application object
   */
  private convert(obj: any): Application{
    let a:Application = new Application();
    a.applicationId = obj.applicationId;
    a.name = obj.name;
    a.icon = obj.icon;
    return a;
  }

  /**
   * Get an Application based on its ID
   */
  public getApplication(applicationId: string): Promise<Application>{
    let headers = new Headers();
    headers.append("Authorization", "Bearer " + this.authService.getUser().accessToken);
    return this.http.get(CONFIG.mobify_api_base_url + "/applications/" + applicationId, { headers: headers })
    .toPromise()
    .then(
      res => {
        if(res.status == 200){
          return this.convert(res.json());
        }else{
          return null;
        }
      }
    )
    .catch(this.handleError);
  }

}
