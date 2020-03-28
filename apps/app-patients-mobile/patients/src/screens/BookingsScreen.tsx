import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import BookingsService from '../services/BookingsService';
import Booking from '../interfaces/Booking';
import { FlatList } from 'react-native-gesture-handler';
import BookingDetails from '../components/BookingDetails';

// const bookingService = new BookingsService({
//   userId: 1
// });

const getBookingData = async () => {
  // const response = await bookingService.getBookings();
  // return response.data;
  return [{
    id: 123,
    date: new Date(),
    status: 'Confirmed',
    bookingWith: {
      id: 1,
      firstName: 'John',
      lastName: 'Smith',
    }
  }]
}

const BookingsScreen = () => {
  const [bookings, setBookings] = useState([]);

  useEffect(() => {
    getBookingData().then((bookings: Booking[]) => {
      setBookings(bookings);
    });
  }, [])

  return (
    <View>
      <Text>Bookings</Text>
      <Text>There are {bookings.length} bookings</Text>
      <FlatList
        keyExtractor={booking => booking.id}
        data={bookings}
        renderItem={({item}) => (
          <BookingDetails {...item}></BookingDetails>
        )}
      />
    </View>
  );
};

const styles = StyleSheet.create({

})

export default BookingsScreen;
