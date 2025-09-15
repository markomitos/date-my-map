import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { catchError, finalize, of, tap } from 'rxjs';
import { DatingService } from '../../../services/dating.service';
import { Period } from '../../../models/period.model';
import { AnswerRequest } from '../../../models/answer.request';
import { DatingResponse } from '../../../models/dating.response.model';
import { PossiblePeriods } from '../../../models/possible.periods.model';

type DatingGameState = 'initial' | 'running' | 'finished';
type DisplayMode = 'quiz' | 'pathFinder';

@Component({
  selector: 'app-map-dater',
  templateUrl: './map-dater-component.html',
  styleUrls: ['./map-dater-component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule],
  standalone: true
})
export class MapDaterComponent {
  private datingApiService = inject(DatingService);
  public isLoading = signal<boolean>(false);
  public error = signal<string | null>(null);
  public displayMode = signal<DisplayMode>('quiz');

  public datingGameState = signal<DatingGameState>('initial');
  private currentSessionId = signal<string | null>(null);
  private currentQuestionId = signal<string | null>(null);
  public currentQuestion = signal<string | null>(null);
  public possiblePeriods = signal<Period[]>([]);
  public finalPeriod: string = "";

  public yearInput = signal<number | null>(null);
  public historicalPath = signal<string[] | null>(null);
  public enabled = signal<boolean>(false);

  public formattedPeriods = computed<string>(() => {
    const periods = this.possiblePeriods();
    if (periods.length === 0) return 'Calculating...';
    return periods.map(p => `[${p.startYear}-${p.endYear}]`).join(', ');
  });

  public setMode(mode: DisplayMode): void {
    if (this.displayMode() === mode) return;
    this.displayMode.set(mode);
    this.error.set(null);

    if (mode === 'quiz') {
      this.restartDating();
    } else {
      this.historicalPath.set(null);
      this.yearInput.set(null);
    }
  }

  public startDating(): void {
    this.isLoading.set(true);
    this.error.set(null);
    this.datingApiService.startDating().pipe(
      tap(response => this.handleBackendResponse(response)),
      catchError(err => this.handleError(err)),
      finalize(() => this.isLoading.set(false))
    ).subscribe();
  }

  public submitAnswer(answer: 'Yes' | 'No'): void {
    const sessionId = this.currentSessionId();
    const questionId = this.currentQuestionId();
    if (!sessionId || !questionId) {
      this.error.set("Session is invalid. Please restart.");
      return;
    }
    const request: AnswerRequest = { sessionId, questionId, answer };
    this.isLoading.set(true);
    this.error.set(null);
    this.datingApiService.processAnswer(request).pipe(
      tap(response => this.handleBackendResponse(response)),
      catchError(err => this.handleError(err)),
      finalize(() => this.isLoading.set(false))
    ).subscribe();
  }

  public restartDating(): void {
    this.datingGameState.set('initial');
    this.currentSessionId.set(null);
    this.currentQuestionId.set(null);
    this.currentQuestion.set(null);
    this.possiblePeriods.set([]);
    this.error.set(null);
  }

  private handleBackendResponse(response: DatingResponse): void {
    this.currentSessionId.set(response.sessionId);
    this.possiblePeriods.set(response.periods);
    if (response.questionId?.startsWith('P')) {
      this.datingGameState.set('finished');
      this.currentQuestion.set(response.message);
      this.currentQuestionId.set(null);
      if (response.questionId != null) {
        this.finalPeriod = response.questionId.slice(1);
      }
    } else {
      this.datingGameState.set('running');
      this.currentQuestion.set(response.message);
      this.currentQuestionId.set(response.questionId);
    }
  }

  public onYearChange(event: Event): void {
    this.historicalPath.set(null);
    this.enabled.set(false);
    const input = event.target as HTMLInputElement;
    let value = input.value;
    if (value.length > 4) {
      value = value.slice(0, 4);
      input.value = value;
    }
    const year = Number(value);

    this.yearInput.set(value === '' || isNaN(year) ? null : year);
    if(year >= 1800 && year <= 2025) this.enabled.set(true);
  }

  public findPathByYear(): void {
    const year = this.yearInput();
    if (!year || year < 1800 || year > 2025) {
      this.error.set("Please enter a valid year between 1800 and 2025.");
      return;
    }

    this.isLoading.set(true);
    this.error.set(null);
    this.historicalPath.set(null);

    this.datingApiService.getHistoricalPath(year).pipe(
      tap(response => {
        this.historicalPath.set(response);
      }),
      catchError(err => this.handleError(err)),
      finalize(() => this.isLoading.set(false))
    ).subscribe();
  }

  private handleError(err: HttpErrorResponse) {
    console.error("API Error:", err);
    this.error.set(err.error?.message || err.message || 'An unknown error occurred. Please try again.');
    return of(null);
  }
}
