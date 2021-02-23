import { Component, OnInit } from '@angular/core';
import { WorkService } from './../services/work.service';
import { environment } from '../../environments/environment';
import { Project } from './../model/Project';

@Component({
  selector: 'app-work',
  templateUrl: './work.component.html',
  styleUrls: ['./work.component.css']
})
export class WorkComponent implements OnInit {
  projectList:Project[];
  baseUrl:string;

  constructor(private workService:WorkService) { }

  ngOnInit(): void {
    this.baseUrl = environment.baseUrl;
    this.workService.getWorkData().subscribe(
      (response:Project[])=>{
        this.projectList = response;
      },
      (error)=>{
        console.log(error);
      }
    );
  }
}
