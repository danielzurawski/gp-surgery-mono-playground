import axios, { AxiosInstance, AxiosResponse } from 'axios';

export default class ApiService {
  private axios: AxiosInstance;

  constructor(baseURL: string, params?: {}) {
    this.axios = axios.create({
      baseURL,
      timeout: 30000,
      params: Object.assign({}, params),
    });
  }

  async get<T>(url: string): Promise<AxiosResponse<T>> {
    return this.axios.get(url);
  }

  async post<T>(url: string, data: any): Promise<AxiosResponse<T>> {
    return this.axios.post(url, data);
  }
}
