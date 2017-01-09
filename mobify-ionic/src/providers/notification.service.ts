import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { Storage } from '@ionic/storage';

import { BasicService } from './basic.service';
import { AuthService } from './auth.service';

import { Notification } from '../model/notification';
import { Application } from '../model/application';

import { CONFIG } from '../app/config';

/**
 * This service class connect to the API to get notifications data
 */
@Injectable()
export class NotificationService extends BasicService {

  constructor(private http: Http, private storage: Storage, private authService: AuthService) {
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
    n.timestampSent = this.parseDate(obj.timestampSent);
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
        let list:Notification[] = new Array(); 
        if(res.status == 200){
          let array = res.json();
          for(let tmp of array){
            list.push(this.convert(tmp));
          }
          return list;
        }else{
          return null;
        }
      }
    )
    .catch(this.handleError);
  }

  /**
   * Get all notifications stored locally
   */
  public getStored(application: Application): Promise<Notification[]>{
    //get all the locally stored notifications
    return this.storage.get('notifications_'+application.applicationId).then(
      jsonArray => {
        let notificationsArray: Notification[] = new Array();
        let objectArray = new Array();
        if(jsonArray != null){
              objectArray = JSON.parse(jsonArray);
              //it could be cleared by the user
              if(objectArray.length > 0){
                for(let obj of objectArray){
                  notificationsArray.push(this.convert(obj));
                }
              }                 
        }
        return notificationsArray;
      }
    );
  }
 
}
