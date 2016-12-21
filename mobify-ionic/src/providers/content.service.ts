import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { BasicService } from './basic.service';

import { NotificationService } from './notification.service';

import { Application } from '../model/application';
import { Notification } from '../model/notification';

/**
 * This class is a singleton to keep the data of the application synchronized
 */
@Injectable()
export class ContentService extends BasicService{

  private applications:Map<String,Application>;
  
  constructor(private notificationService: NotificationService) {
    super();
  }
  
  public init(): Promise<boolean> {
    console.log('initializing mobify data');
    this.applications = new Map<String,Application>();
    return this.notificationService.getUnreceived()
      .then(data => {        
         for(let n of data){
             if(this.applications.has(n.applicationId) == false){
               let app: Application = new Application();
               app.applicationId = n.applicationId;
               app.name = '__TEMP__';
               this.applications.set(n.applicationId, app);
             }
             this.applications.get(n.applicationId).notifications.push(n);
             this.applications.get(n.applicationId).lastMessageText = n.message;
          }   
          return true;   
     })
     .catch(this.handleError);
  }
  
  public getApplications(): IterableIterator<Application> {
    return this.applications.values();
  }

}
