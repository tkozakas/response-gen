package org.response.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tomas Kozakas
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestMessage {
    private String role;
    private String content;
}
