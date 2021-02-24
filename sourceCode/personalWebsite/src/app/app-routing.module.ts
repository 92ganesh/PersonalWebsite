import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { WorkComponent } from './work/work.component';
import { JourneyComponent } from './journey/journey.component';

const routes: Routes = [
  { path:'home', component:HomeComponent},
  { path:'work', component:WorkComponent},
  { path:'journey', component:JourneyComponent},
  { path:'', redirectTo:'home',pathMatch:'full'},
  { path:'**', redirectTo:'home'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
