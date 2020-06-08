import xlsxFile from 'read-excel-file'
import * as moment from "moment";


function isNumeric(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}
export default function parseExcel(file) {

    return new Promise((resolve, reject) => {
       xlsxFile(file).then(data => {
            let headers = data[0];
            data = data.slice(1, data.length);
            const legendHeader = headers[0];
            headers = headers.slice(1, headers.length);
            const legend = [];
            const dataErrors = []

            let isDate = false;
            let isNumber = true;

            for (let i = 0; i < data.length; i++) {
                let row = data[i];
                legend.push(row[0]);
                console.log('[ExcelParserJs].parseExcel row:', row[0], isNumeric(row[0]), moment(row[0], 'dd.MM.yyyy').date().valueOf());


                isNumber = isNumber && isNumeric(row[0]);

                if(!isNumber){
                    if(!isNaN(moment(row[0], 'dd.MM.yyyy').date().valueOf()))
                        isDate = true
                }

                data[i] = row.slice(1, row.length);
                row = data[i];

                for (let j = 0; j < row.length; j++) {
                    if(!isNumeric(row[j])){
                        dataErrors.push({error: "IS_NOT_NUMBER", row: i, column: j});
                    }
                }
            }


            resolve({
                data,
                headers,
                legendHeader,
                legend,
                dataErrors,
                isDate,
                isNumber
            })


        }).catch(reason => {
            reject(reason)
        })

    });
}
