export interface Task {
  id?: number;
  title: string;
  description?: string;
  dueDate: string; // ISO format (yyyy-mm-dd)
  status: 'PENDING' | 'COMPLETED';
}
