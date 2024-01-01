import dayjs from 'dayjs/esm';

export interface ITopiaUser {
  id?: number;
  name?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  dateOfBirth?: dayjs.Dayjs | null;
  userName?: string | null;
  email?: string | null;
  createdDate?: dayjs.Dayjs | null;
  createdBy?: number | null;
  updatedDate?: dayjs.Dayjs | null;
  updatedBy?: number | null;
  status?: string | null;
}

export class TopiaUser implements ITopiaUser {
  constructor(
    public id?: number,
    public name?: string | null,
    public firstName?: string | null,
    public lastName?: string | null,
    public dateOfBirth?: dayjs.Dayjs | null,
    public userName?: string | null,
    public email?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public createdBy?: number | null,
    public updatedDate?: dayjs.Dayjs | null,
    public updatedBy?: number | null,
    public status?: string | null
  ) {}
}

export function getTopiaUserIdentifier(topiaUser: ITopiaUser): number | undefined {
  return topiaUser.id;
}
