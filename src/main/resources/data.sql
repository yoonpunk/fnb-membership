INSERT INTO member VALUES
                       (UNHEX('7ca3402c47c243df93e15469124ebc79'), '123456789012', SYSDATE(), '01042345678'),
                       (UNHEX('8ca3402c47c243df93e15469124ebc79'), '234567890123', SYSDATE(), '01023456789'),
                       (UNHEX('9ca3402c47c243df93e15469124ebc79'), '345678901234', SYSDATE(), '01034567890');

INSERT INTO brand VALUES
                      (UNHEX('896085a6b13a11edafa10242ac120002'), SYSDATE(), 'CAFE'),
                      (UNHEX('90e30916b13a11edafa10242ac120002'), SYSDATE(), 'BAKERY'),
                      (UNHEX('961524a0b13a11edafa10242ac120002'), SYSDATE(), 'FOOD');

INSERT INTO store VALUES
                      (UNHEX('0c397524b13a11edafa10242ac120002'), SYSDATE(), '20percent', UNHEX('896085a6b13a11edafa10242ac120002')),
                      (UNHEX('aa397524b13a11edafa10242ac120002'), SYSDATE(), 'seoulbbang', UNHEX('90e30916b13a11edafa10242ac120002')),
                      (UNHEX('bb397524b13a11edafa10242ac120002'), SYSDATE(), 'gimbabnara', UNHEX('961524a0b13a11edafa10242ac120002'));