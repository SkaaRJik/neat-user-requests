import xlsxFile from 'read-excel-file'
import * as moment from "moment";
import _ from "lodash"

function isString(value) {
    return typeof value === 'string' || value instanceof String;
}

export default function parseExcel(file) {

    return new Promise((resolve, reject) => {
        xlsxFile(file).then(data => {
            let headers = data[0];
            data = data.slice(1, data.length);
            const legendHeader = headers[0];
            headers = headers.slice(1, headers.length);
            let legend = [];
            const dataErrors = []
            let increment = 0;
            let isAllDates = true;

            for (let i = 0; i < data.length; i++) {
                let row = data[i];
                legend.push(row[0]);

                if (!isString(row[0])) {
                    try {
                        increment += row[0];
                    } catch (e) {
                        increment = 0;
                    }
                } else {
                    increment = 0
                }

                if (moment(row[0]).toDate().getTime()) {
                    isAllDates = isAllDates && true;
                } else {
                    isAllDates = false
                }


                data[i] = row.slice(1, row.length);
                row = data[i];

                for (let j = 0; j < row.length; j++) {
                    if (!_.isNumber(row[j])) {
                        dataErrors.push({error: "ERROR_IS_NOT_NUMBER", row: i, column: j});
                    }
                }
            }

            if (increment) {
                try {
                    increment /= legend.length;
                } catch (e) {
                    console.error('[ExcelParser]. increment error:', e);
                    increment = 0;
                }
            } else {
                increment = 0
            }

            if (isAllDates && legend.length > 0) {
                increment = 0;
                legend = legend.map((value) => {
                    const date = moment(value).toDate()
                    increment += date.getTime()
                    return date;
                })
                if (increment) {
                    increment /= legend.length;
                }
            }

            if (increment === 0) {
                dataErrors.push({error: "ERROR_LEGEND_DOESNT_HAVE_INCREMENT"});
            }


            resolve({
                data,
                headers,
                legendHeader,
                legend,
                dataErrors,
                increment,
                isAllDates
            })


        }).catch(reason => {
            reject(reason)
        })

    });
}
