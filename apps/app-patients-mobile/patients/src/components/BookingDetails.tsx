import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

const BookingDetails = (props) => {
  return (
    <View>
      <Text>Booking with: {props.bookingWith.firstName} {props.bookingWith.lastName}</Text>
      <Text>Booking date: {props.date.toString()}</Text>
      <Text>Status: {props.status}</Text>
    </View>
  )
}

export default BookingDetails;
