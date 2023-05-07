package com.vetrix.chat_API.live;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Message {
    private String sender;
    private String receiver;
    private String message;
}
