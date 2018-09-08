/*
 * NRF24_RX.cpp
 *
 * Created: 15/08/2014 22:21:15
 *  Author: Miro
 */ 

#include "Arduino.h"
#include "config.h"
#include "def.h"
#include "types.h"
#include "MultiWii.h"
#include "v202_protocol.h"

#if defined(NRF24_V202_RX)


// Set up nRF24L01 radio on SPI bus plus pins 9 & 10

static nrf24l01p radio;
static v202Protocol protocol;
static rx_values_t rxValues;
uint8_t nrf24_rxState = NOT_BOUND;
int16_t nrf24_rcData[RC_CHANS];


// Single radio pipe address for the 2 nodes to communicate.
static const uint64_t pipe = 0xE8E8F0F0E1LL;


void NRF24_Init() {
	
	// SS pin must be set as output to set SPI to master !
	pinMode(SS, OUTPUT);
  
	radio.setPins(NRF24_PIN_CE,NRF24_PIN_CS);
	protocol.init(&radio);
}


void NRF24_Read_RC() {
	
	nrf24_rxState = protocol.run(&rxValues);
	
	switch( nrf24_rxState )
	{
		case  BIND_IN_PROGRESS:
			debug[0] = 1;
			//Serial.println("Bind in progress");
		break;
	  
		case BOUND_NEW_VALUES:
			debug[1] = 1;
			nrf24_rcData[ROLL] = map(rxValues.roll, -127, 127, 1000, 2000);
			nrf24_rcData[PITCH] = map(rxValues.pitch, -127, 127, 1000, 2000);
			nrf24_rcData[THROTTLE] = map(rxValues.throttle, 0, 255, 1000, 2000);
			nrf24_rcData[YAW] = map(rxValues.yaw, -127, 127, 1000, 2000);
      
			nrf24_rcData[AUX1] = rxValues.trim_roll*100+1000;
			nrf24_rcData[AUX2] = rxValues.trim_pitch*100+1000;
			nrf24_rcData[AUX3] = rxValues.trim_yaw*100+1000;
			nrf24_rcData[AUX4] = rxValues.flags*2000;
		/*	rcData[AUX1] = Servo_Buffer[4];
			rcData[AUX2] = Servo_Buffer[5];
			rcData[AUX3] = Servo_Buffer[6];
			rcData[AUX4] = Servo_Buffer[7];
			*/
		break;
	  
		case BOUND_NO_VALUES:
		//Serial.print(newTime - time); Serial.println(" : ----"); // 32ms for 16Mhz
		break;
	  
		default:
		break;
	}	
	
	
}


#endif
