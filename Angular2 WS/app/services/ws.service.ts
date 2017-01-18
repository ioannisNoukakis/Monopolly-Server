import { Component,Injectable } from '@angular/core';
import {Observable} from 'rxjs/Rx';

@Injectable()
export class WsService {

    private websocket: any;
    private receivedMsg: any;
    
    public sendMessage(text:string){
      this.websocket.send(text);
    }

    public doAuth(text:string[]): Observable<any>{
      this.websocket = new WebSocket("ws://localhost:8080/api/ws");
      this.websocket.onopen =  (evt) => {
          text.forEach((msg) => {
              this.websocket.send(msg);
          });
      };

      return Observable.create(observer=>{
          this.websocket.onmessage = (evt) => { 
              observer.next(evt);
          };
      })
      .map(res=> res.data)
      .share();
    }

}