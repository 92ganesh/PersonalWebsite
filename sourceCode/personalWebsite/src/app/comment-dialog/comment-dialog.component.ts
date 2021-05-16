import { Component, OnInit } from '@angular/core';
import { faWindowClose } from '@fortawesome/free-solid-svg-icons';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { EmailService } from './../services/email.service';
import { environment } from '../../environments/environment';
import { EmailAPI } from './../model/EmailAPI';

@Component({
  selector: 'app-comment-dialog',
  templateUrl: './comment-dialog.component.html',
  styleUrls: ['./comment-dialog.component.css']
})
export class CommentDialogComponent implements OnInit {
  baseUrl:string;
  windowClose = faTimes;
  emailAPIendpoint:string;
  blank:string = '';

  constructor(private emailService:EmailService) { }

  ngOnInit(): void {
  }

  submit(details:{ name:string, contact:string, message:string}){
    this.baseUrl = environment.baseUrl;
    this.emailService.getEmailAPI().subscribe(
      (response:EmailAPI)=>{
        this.emailAPIendpoint = response.emailAPIendpoint;
        this.emailService.sendCommentDetails(this.emailAPIendpoint,details).subscribe(
          (response2)=>{
            // commentDetails sent successfully
          }
        );
      },
      (error)=>{
        console.log(error);
      }
    );

    document.getElementById('closeCommentDialog').click();
  }

}
