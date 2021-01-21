export interface SectionModel {
  id?:number ;
  title: string;
  subtitle: string;
  slug: string;
  createdBy?:  string;
  updatedBy?: string;
  articlesQtt?: number;
  updatedAt?: Date;
  createdAt?: Date;
  showMore?: boolean;
  selected?: boolean;
}