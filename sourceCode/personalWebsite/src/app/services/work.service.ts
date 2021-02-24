import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WorkService {

  constructor(private http:HttpClient) { }
  
  public getWorkData(){
    return this.http.get(environment.baseUrl+'work.json');
  }
}