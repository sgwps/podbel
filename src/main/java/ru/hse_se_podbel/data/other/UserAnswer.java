package ru.hse_se_podbel.data.other;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserAnswer{
    List<String> options;
    String task;  // Task id
}
