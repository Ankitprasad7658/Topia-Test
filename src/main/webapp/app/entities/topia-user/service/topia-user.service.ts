import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITopiaUser, getTopiaUserIdentifier } from '../topia-user.model';

export type EntityResponseType = HttpResponse<ITopiaUser>;
export type EntityArrayResponseType = HttpResponse<ITopiaUser[]>;

@Injectable({ providedIn: 'root' })
export class TopiaUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/topia-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(topiaUser: ITopiaUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(topiaUser);
    return this.http
      .post<ITopiaUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(topiaUser: ITopiaUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(topiaUser);
    return this.http
      .put<ITopiaUser>(`${this.resourceUrl}/${getTopiaUserIdentifier(topiaUser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(topiaUser: ITopiaUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(topiaUser);
    return this.http
      .patch<ITopiaUser>(`${this.resourceUrl}/${getTopiaUserIdentifier(topiaUser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITopiaUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITopiaUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTopiaUserToCollectionIfMissing(
    topiaUserCollection: ITopiaUser[],
    ...topiaUsersToCheck: (ITopiaUser | null | undefined)[]
  ): ITopiaUser[] {
    const topiaUsers: ITopiaUser[] = topiaUsersToCheck.filter(isPresent);
    if (topiaUsers.length > 0) {
      const topiaUserCollectionIdentifiers = topiaUserCollection.map(topiaUserItem => getTopiaUserIdentifier(topiaUserItem)!);
      const topiaUsersToAdd = topiaUsers.filter(topiaUserItem => {
        const topiaUserIdentifier = getTopiaUserIdentifier(topiaUserItem);
        if (topiaUserIdentifier == null || topiaUserCollectionIdentifiers.includes(topiaUserIdentifier)) {
          return false;
        }
        topiaUserCollectionIdentifiers.push(topiaUserIdentifier);
        return true;
      });
      return [...topiaUsersToAdd, ...topiaUserCollection];
    }
    return topiaUserCollection;
  }

  protected convertDateFromClient(topiaUser: ITopiaUser): ITopiaUser {
    return Object.assign({}, topiaUser, {
      dateOfBirth: topiaUser.dateOfBirth?.isValid() ? topiaUser.dateOfBirth.format(DATE_FORMAT) : undefined,
      createdDate: topiaUser.createdDate?.isValid() ? topiaUser.createdDate.format(DATE_FORMAT) : undefined,
      updatedDate: topiaUser.updatedDate?.isValid() ? topiaUser.updatedDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? dayjs(res.body.dateOfBirth) : undefined;
      res.body.createdDate = res.body.createdDate ? dayjs(res.body.createdDate) : undefined;
      res.body.updatedDate = res.body.updatedDate ? dayjs(res.body.updatedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((topiaUser: ITopiaUser) => {
        topiaUser.dateOfBirth = topiaUser.dateOfBirth ? dayjs(topiaUser.dateOfBirth) : undefined;
        topiaUser.createdDate = topiaUser.createdDate ? dayjs(topiaUser.createdDate) : undefined;
        topiaUser.updatedDate = topiaUser.updatedDate ? dayjs(topiaUser.updatedDate) : undefined;
      });
    }
    return res;
  }
}
