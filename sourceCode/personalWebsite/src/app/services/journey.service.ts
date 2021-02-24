import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class JourneyService {

  constructor(private http:HttpClient) { }

  public getJourneyData(){
    return this.http.get(environment.baseUrl+'myjourney.json');
  }
}