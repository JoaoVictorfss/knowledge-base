export interface ArticleModel {
  id?: number;
  title: String;
  subtitle: string;
  content: string;
  slug: string;
  viewers: number;
  createdBy: string;
  updatedBy: string;
  status: string;
  categoriesId:number[];
  averageLiked: number;
  greatLiked: number;
  poorLiked: number;
  sectionId?: number;
  updatedAt: Date;
  createdAt: Date;
}