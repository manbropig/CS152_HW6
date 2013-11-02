(define proc
  (lambda (a b)
    (let ((sum (+ a (func b))))
      sum)))
      
(define func
  (lambda (a)
    (let* ((b 2)
           (prod (* a b)))
      prod)))


(define x 2)
(define y 3)
(proc x y)


