import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { BasicService } from './basic.service';
import { AuthService } from './auth.service';

import { Notification } from '../model/notification';

import { CONFIG } from '../app/config';

/**
 * This service class connect to the API to get notifications data
 */
@Injectable()
export class NotificationService extends BasicService {

  constructor(private http: Http, private authService: AuthService) {
    super();
  }
  
  /**
   * Convert a notification value object received from the backend to a Notification object
   */
  private convert(obj: any): Notification{
    let n:Notification = new Notification();
    n.notificationId = obj.notificationId;
    n.applicationId = obj.applicationId;
    n.type = obj.type;
    n.message = obj.message;
    n.timestampSent = obj.timestampSent;
    n.read = (obj.timestampRead != null) ? true : false;
    return n;
  }

  /**
   * Get all unreceived notifications from the backend
   */
  public getUnreceived(): Promise<Notification[]>{
    let headers = new Headers();
    headers.append("Authorization", "Bearer " + this.authService.getUser().accessToken);
    return this.http.get(CONFIG.mobify_api_base_url + "/notifications/unreceived", { headers: headers })
    .toPromise()
    .then(
      res => {
        console.log(res.json());
        let list:Notification[] = new Array(); 
        if(res.status == 200){
          let array = res.json();
          for(let tmp of array){
            list.push(this.convert(tmp));
          }
          console.log(list);
          return list;
        }else{
          return null;
        }
      }
    )
    .catch(this.handleError);
  }
  
}
