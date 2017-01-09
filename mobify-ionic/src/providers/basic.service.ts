/**
 * Basic functions for each service
 */
export class BasicService {
  
  constructor() {
  }
  
  public handleError(err){
    console.log(err);
  }

  public parseDate(strDate:string):Date{
    return new Date(strDate);
  }
}