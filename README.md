# Relational Query Language (RQL)

## Introduction

RQL is a query language created by Lucas Venezian Povoa [1] based on the Original Relational Algebra by E.F. Codd and the A Relational Algebra by C.J. Date and H. Darwen. It aims at defining an actual abstract, mathematical based query language for the actual Relational Model.

RQL2SQL is a framework wrote in Java that aims at translating Relational Query Language [1] code into Structured Query Language code.

### Exemples

Considering the following conceptual data model.

```
Sale(idSale, date, time, idClient) -[1:n]- SaleItem(idSale, idProduct) -[n:1]- Product(idProduct, name)
```

The question "Which are the Products related to all Sales?" could be answer in SQL as: 

```SQL
SELECT DISTINCT idProdut
FROM SaleItem
WHERE 
    (
	SELECT COUNT(idSale)
	FROM Sale
    ) = 
    (
	SELECT COUNT(*)
	FROM SaleItem AS si
	WHERE si.idProduto = SaleItem.idProdut
    )
```

and in RQL as:

```SQL
¢ idProduto, idVenda (ItensVenda) / ¢ idVenda (Venda)
```
## References

[1] Lucas Venezian Povoa. Relational Query Language: a purely relational query language (portuguese). Graduation Thesis. Supervisor: João Maurício Hipólito. 2011.
