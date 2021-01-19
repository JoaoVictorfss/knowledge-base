export interface CategoryModel {
  id?: number;
  title: string;
  createdBy: string;
  updatedBy: string;
  subtitle: string;
  slug ?: string;
  updatedAt: Date;
  createdAt: Date;
  articlesQtt: number;
  sectionsQtt: number;
}