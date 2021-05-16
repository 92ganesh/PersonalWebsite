import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { CommentDetails } from './../model/CommentDetails'

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  constructor(private http:HttpClient) { }
  
  public getEmailAPI(){
    return this.http.get(environment.baseUrl+'emailAPI.json');
  }

  public sendCommentDetails(emailAPIendpoint:string,commentDetails:CommentDetails){
    return this.http.post(emailAPIendpoint,commentDetails);
  }
}