import { Component, OnInit } from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import { CommentDialogComponent } from './../comment-dialog/comment-dialog.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  commentBoxText="Leave a message for me...";
  constructor(public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  openDialog() {
    const dialogRef = this.dialog.open(CommentDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      
    });
  }
}