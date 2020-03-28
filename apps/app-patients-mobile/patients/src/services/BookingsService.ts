import ApiService from "./ApiService";
import { AxiosResponse } from "axios";
import Booking from "../interfaces/Booking";

type ServiceConfig = {
  userId: number;
}

export default class BookingsService extends ApiService {
  constructor(config: ServiceConfig) {
    super('/bookings', {
      userId: config.userId
    });
  }

  getBookings(): Promise<AxiosResponse<Booking[]>> {
    return this.get('/');
  }

  getBooking(id: number): Promise<AxiosResponse<Booking>> {
    return this.get(`/${id}`);
  }

  createBooking(bookingData: Booking): Promise<AxiosResponse<Booking>> {
    return this.post('/', bookingData);
  }

  updateBooking(bookingData: Booking): Promise<AxiosResponse<Booking>> {
    return this.post(`/${bookingData.id}`, bookingData);
  }
}
