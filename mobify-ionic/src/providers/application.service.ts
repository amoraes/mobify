import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { BasicService } from './basic.service';
import { AuthService } from './auth.service';

import { Application } from '../model/application';

import { CONFIG } from '../app/config';

/**
 * This service class connect to the API to get Application and User Application Settings data
 */
@Injectable()
export class ApplicationService extends BasicService {

  constructor(private http: Http, private authService: AuthService) {
    super();
  }
  
  /**
   * Convert a settings value object received from the backend to an Applications object
   */
  private convert(obj: any): Application{
    let app:Application = new Application();
    app.applicationId = obj.application.applicationId;
    app.name = obj.application.name;
    app.icon = obj.application.icon;
    app.silent = obj.silent;
    return app;
  }

  /**
   * Get all user settings
   */
  public getAll(): Promise<Application[]>{
    let headers = new Headers();
    headers.append("Authorization", "Bearer " + this.authService.getUser().accessToken);
    return this.http.get(CONFIG.mobify_api_base_url + "/user/applications/settings", { headers: headers })
    .toPromise()
    .then(
      res => {
        let list:Application[] = new Array(); 
        if(res.status == 200){
          let array = res.json();
          for(let tmp of array){
            list.push(this.convert(tmp));
          }
          return list;
        }
      }
    )
    .catch(this.handleError);
  }

  /**
   * Update application settings
   */
  public updateApplicationSettings(applicationId:string, silent:boolean): void{
    let headers = new Headers();
    headers.append("Authorization", "Bearer " + this.authService.getUser().accessToken);
    this.http.put(CONFIG.mobify_api_base_url + "/user/applications/"+applicationId+"/settings", 
                    { silent: silent },
                    { headers: headers })
                  .toPromise()  
                  .catch(this.handleError);  
  }
  
}
