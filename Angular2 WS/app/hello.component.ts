import { Component } from '@angular/core';
import { AuthModel } from './models/auth.model';
import { WsService } from './services/ws.service';

@Component({
  selector: 'hello',
  providers:[WsService],
  templateUrl: 'hello.component.html'
})
export class HelloComponent { 
/**
 * En gros tu vas toujours recevoir un code qui te dira de quoi ca sagit:
 * UserAnswer: un user a donné une réponse.
 * wsResponse: C'est un message d'erreur.
 * addQuestion: C'est une question ajoutée
 * 
 * Tu peux utiliser la méthode sendMessage pour envoyer un message mais préfère le doAuth en premier et attends qu'il te réponde pour pouvoir 
 * utilise sendMessage.
 * 
 * Après reste les messages.
 * 
 * en premier le token
 * Pour l'enpoint answersReply tu dois spécifier la questionId que tu veux recevoir les notifications
 * Pour l'enpointquestionPost tu dois spécifier le roomId que tu veux recevoir les notifications
 * enfin si tu veux t'abonner ou te désabonner.
 * 
 * Si tu te déco ça t'enlevera des abonnements.
 * 
 * gl hf
 */
  constructor(private wsService: WsService){
        var tab: string[] = [];
        tab.push(JSON.stringify(new AuthModel("eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoMCIsImlkIjoiMSJ9.JSe-mnPnvGCKOufDWDCj4fK5ZQ5hK2J90kaKiFCZu48",
                                                            "answersReply", 2, true)));
        tab.push(JSON.stringify(new AuthModel("eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoMCIsImlkIjoiMSJ9.JSe-mnPnvGCKOufDWDCj4fK5ZQ5hK2J90kaKiFCZu48",
                                                            "questionPost", 3, true)));
        //faut attenre le premier résuiltat avant de refaire un appel avec wsService.sendMessage
        this.wsService.doAuth(tab).subscribe((result) => {
            console.log(result);
        });
    }
}