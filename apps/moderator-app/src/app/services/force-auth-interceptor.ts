import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {AuthService} from './auth.service';
import {Router} from '@angular/router';
import {ModerationService} from './moderation.service';

@Injectable({providedIn: 'root'})
export class ForceAuthInterceptor implements HttpInterceptor {
  constructor(private router: Router,
              private authService: AuthService,
              private moderationService: ModerationService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req)
      .pipe(
        tap((event: HttpEvent<any>) => {
          if (event instanceof HttpResponse) {
            // nothing
          }
        },
        (err: any) => {
          if (err.status === 401) {
            this.moderationService.endPolling();
            this.authService.clearToken();
            window.history.go()
          }
        }
      )
    );
  }
}
