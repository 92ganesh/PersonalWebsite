import { Component, OnInit } from '@angular/core';
import { Milestone } from './../model/milestone'
import { JourneyService } from '../services/journey.service';

@Component({
  selector: 'app-journey',
  templateUrl: './journey.component.html',
  styleUrls: ['./journey.component.css']
})
export class JourneyComponent implements OnInit {
  displayBlockOnLeftSide:boolean = true;
  milestonesList:Milestone[];
  constructor(private journeyService:JourneyService) { }

  ngOnInit(): void {
    this.journeyService.getJourneyData().subscribe(
      (response:Milestone[]) =>{
        this.milestonesList=response;
        for(let i=0; i<this.milestonesList.length; i++){
          this.milestonesList[i].displaySide = (this.displayBlockOnLeftSide)?'left':'right';
          this.displayBlockOnLeftSide=!this.displayBlockOnLeftSide;
        }
        console.log(this.milestonesList);
      },
      (error)=>{

      }
    );
  }
}