import xlsxFile from "read-excel-file";
import _ from "lodash";

function isIsString(value) {
  return typeof value === "string" || value instanceof String;
}

export default function parseExcel(file) {
  return new Promise((resolve, reject) => {
    xlsxFile(file)
      .then(data => {
        let headers = data[0];
        data = data.slice(1, data.length);
        const legendHeader = headers[0];
        headers = headers.slice(1, headers.length);
        const legend = [];
        const dataErrors = [];
        let increment = 0;
        let isDate = false;
        let isNumber = false;
        let isString = false;

        for (let i = 0; i < data.length; i++) {
          let row = data[i];
          legend.push(row[0]);

          if (isIsString(row[0])) {
            isString = true;
            increment = 0;
            dataErrors.push({
              error: "ERROR_CANT_BE_STRING",
              row: i,
              value: row[0]
            });
          }

          if (_.isDate(row[0])) {
            isDate = true;
          }

          if (_.isNumber(row[0])) {
            isNumber = true;
          }

          if (isNumber && isDate) {
            dataErrors.push({ error: "ERROR_CANT_MIX_LEGEND_DATA", row: i });
          } else {
            try {
              if (i > 0) {
                increment += row[0] - legend[i - 1];
              }
            } catch (e) {
              console.error("[ExcelParser].if(isNumber && isDate) error :", e);
              increment = 0;
            }
          }

          if (isString) {
            increment = 0;
          }

          data[i] = row.slice(1, row.length);
          row = data[i];

          for (let j = 0; j < row.length; j++) {
            if (!_.isNumber(row[j])) {
              dataErrors.push({
                error: "ERROR_IS_NOT_NUMBER",
                row: legend[i],
                column: headers[j]
              });
            }
          }
        }

        if (increment <= 0) {
          dataErrors.push({ error: "ERROR_LEGEND_DOESNT_HAVE_INCREMENT" });
        } else {
          increment /= legend.length;
        }

        resolve({
          data,
          headers,
          legendHeader,
          legend,
          dataErrors,
          increment,
          isDate
        });
      })
      .catch(reason => {
        console.error("[ExcelParser].catch(reason):", reason);
        reject("ERROR_CANT_PROCESS_FILE");
      });
  });
}
