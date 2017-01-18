export class AuthModel {
  constructor(
    public token: string,
    public endpoint: string,
    public subOjbectId: number,
    public subscribe: boolean
  ) {  }
}