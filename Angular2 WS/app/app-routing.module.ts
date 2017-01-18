import { NgModule }     from '@angular/core';
import { RouterModule } from '@angular/router';
import { AppComponent } from './app.component'
import { HelloComponent } from './hello.component'

@NgModule({
  imports: [
    RouterModule.forRoot([
      { path: '', component: HelloComponent }
    ])
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {}
