const objectstocsv = require('objects-to-csv')
const faker = require('faker')

let data = []
let number = 100

for(let i=0;i<number;i++){
    let password = faker.internet.password();
    data.push(
        {
            USERNAME:faker.name.firstName() + faker.random.word(),
            PASSWORD: password,
            PASSWORD_CHECK: password
        }
    )
}

// convert to csv file

const csv = new objectstocsv(data);

// Save to file:
csv.toDisk('./Users.csv');