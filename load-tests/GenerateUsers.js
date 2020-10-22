const objectstocsv = require('objects-to-csv')
const faker = require('faker')

let data = []
let number = 100

for(let i=0;i<number;i++){
    let password = "P@ssword123"
    let name = faker.name.firstName();
    data.push(
        {
            USERNAME: name + faker.random.word(),
            FIRSTNAME: name,
            LASTNAME: faker.name.lastName(),
            EMAIL: faker.internet.email(name),
            PASSWORD: password,
            PASSWORD_CHECK: password
        }
    )
}

// convert to csv file

const csv = new objectstocsv(data);

// Save to file:
csv.toDisk('./Users.csv');
