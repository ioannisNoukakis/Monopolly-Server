import { NgModule }          from '@angular/core';
import { BrowserModule }     from '@angular/platform-browser';
import { FormsModule }       from '@angular/forms';
import { RouterModule }      from '@angular/router';
import { AppComponent }      from './app.component';
import { NgbModule }         from '@ng-bootstrap/ng-bootstrap';
import { HelloComponent } from './hello.component'


import { AppRoutingModule }  from './app-routing.module';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
	  NgbModule.forRoot()
  ],
  declarations: [
    AppComponent,
    HelloComponent
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule {
}
