interface IData{
  username: string,
  token: string
}

export interface AuthModel{
  data: IData,
  errors: string[]
}