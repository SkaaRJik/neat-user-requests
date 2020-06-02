package ru.filippov.neat.parser.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExcelFile {
    private List<String> headers;
    private List<List<Object>> data;
    private List<Object> legend;
    private String legendHeader;
    private List<String> dataErrors;
}
