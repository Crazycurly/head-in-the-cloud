/*
 * NRF24_RX.h
 *
 * Created: 15/08/2014 22:21:38
 *  Author: Miro
 */ 


#ifndef NRF24_RX_H_
#define NRF24_RX_H_

#include "config.h"
#include "v202_protocol.h"

#if defined(NRF24_V202_RX)

extern uint8_t nrf24_rxState;
extern int16_t nrf24_rcData[RC_CHANS];

void NRF24_Init();
void NRF24_Read_RC();

#endif

#endif /* NRF24_RX_H_ */