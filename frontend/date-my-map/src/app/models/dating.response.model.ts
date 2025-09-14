import {PossiblePeriods} from './possible.periods.model';
import {Period} from './period.model';

export interface DatingResponse {
  message: string | null;
  sessionId: string;
  periods: Period[];
  questionId: string | null;
}
