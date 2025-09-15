import {inject, Injectable} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {DatingResponse} from '../models/dating.response.model';
import {AnswerRequest} from '../models/answer.request';


@Injectable({
  providedIn: 'root'
})
export class DatingService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/dating';


  /** Starts a new dating session. */
  startDating(): Observable<DatingResponse> {
    return this.http.get<DatingResponse>(`${this.apiUrl}/start`);
  }

  /** Submits a user's answer to the current question. */
  processAnswer(request: AnswerRequest): Observable<DatingResponse> {
    return this.http.post<DatingResponse>(`${this.apiUrl}/answer`, request);
  }

  /** Finds the historical decision path for a given year. */
  getHistoricalPath(year: number): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/path/${year}`);
  }

}
